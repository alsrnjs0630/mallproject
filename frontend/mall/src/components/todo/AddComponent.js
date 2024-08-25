import {useState} from "react";
import {postAdd} from "../../api/todoApi";
import ResultModal from "../common/ResultModal";
import useCustomMove from "../../hooks/useCustomMove";

const initState = {
    title:'',
    author:'',
    dueDate:''  // 폼의 초기 상태
}

const AddComponent = () => {

    // 상태 관리 훅
    const [todo, setTodo] = useState({...initState})

    // 결과 데이터가 있는 경우에는 ResultModal을 보여준다.
    const [result, setResult] = useState(null) //결과 상태

    const {moveToList} = useCustomMove() // useCustomMove 활용 modal 창이 닫히면 목록 페이지로 이동
    /*
       * 스프레드 연산자('...')
       * 배열이나 객체의 요소들을 개별적으로 분리하여 새로운 배열이나 객체로 복사
       * initState의 모든 키와 값을 그대로 복사 But 새로운 객체는 initState와는 서로 다른 메모리 주소를 가진다. 즉 두 객체는 서로 독립적
       * 복사된 객체는 'todo'라는 상태로 사용됨
       * 복사 이유 -> 불변성(Immutability) 유지를 위해 -> 예상치 못한 동작 방지
       * 새로운 객체 생성 -> 참조(reference)값 변경으로 컴포넌트를 다시 렌더링하게함.
       * */

    const handleChangeTodo = (e) => {
        todo[e.target.name] = e.target.value
        setTodo({...todo})

    }

    const handleClickAdd = () => {
        // console.log(todo)
        postAdd(todo)
            .then(result => {
                console.log(result)
                setResult(result.Tno) // 결과 데이터 변경
                // 초기화
                setTodo({...initState})
            }).catch(e => {
                console.error(e)
        })
    }

    const closeModal = () => {
        setResult(null)
        moveToList() // Modal창이 닫히면 목록페이지로 이동
    }

    return (
        <div className="border-2 border-sky-200 mt-10 m-2 p-4">
            {/* 모달 처리 */}
            {result ? <ResultModal title={'Add Result'} content={`New ${result} Added`}
                                   callbackFn={closeModal}/> : <></> }

            <div className={'flex justify-center'} >
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">TITLE</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                           name="title"
                           type={'text'}
                           value={todo.title}
                           onChange={handleChangeTodo}
                    >
                    </input>
                </div>
            </div>

            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">AUTHOR</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                           name="author"
                           type={'text'}
                           value={todo.author}
                           onChange={handleChangeTodo}
                    >
                    </input>
                </div>
            </div>

            <div className="flex justify-center">
                <div className="relative mb-4 flex w-full flex-wrap items-stretch">
                    <div className="w-1/5 p-6 text-right font-bold">DUEDATE</div>
                    <input className="w-4/5 p-6 rounded-r border border-solid border-neutral-500 shadow-md"
                           name="dueDate"
                           type={'date'}
                           value={todo.dueDate}
                           onChange={handleChangeTodo}
                    >
                    </input>
                </div>
            </div>

            <div className="flex justify-end">
                <div className="relative mb-4 flex p-4 flex-wrap items-stretch">
                    <button type="button"
                            className="rounded p-4 w-36 bg-blue-500 text-xl text-white"
                            onClick={handleClickAdd}
                            >
                        ADD
                    </button>
                </div>
            </div>
        </div>
    )
}

export default AddComponent;