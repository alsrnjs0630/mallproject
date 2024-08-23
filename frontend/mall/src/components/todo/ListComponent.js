import { useEffect, useState } from "react";
import {getList} from "../../api/todoApi";
import useCustomMove from "../../hooks/useCustomMove";
import PageComponent from "../common/PageComponents";

const initState = {
    dtoList: [],
    pageNumList: [],
    pageRequestDTO: null,
    prev: false,
    next: false,
    totalCount: 0,
    prevPage: 0,
    nextPage: 0,
    totalPage: 0,
    current: 0    // 초기 객체 상태
}

const ListComponent = () => {

    // useCustomMove 훅을 사용하여 현재 쿼리 스트링에서 page와 size 값을 가져옴
    const {page, size, refresh, moveToList, moveToRead} = useCustomMove()

    // serverData 는 나중에 사용
    const [serverData, setServerData] = useState(initState)

    // page 또는 size 값이 변경될 때마다 서버에서 새로운 데이터를 가져옴.
    useEffect(() => {
        // 서버로부터 page와 size를 기준으로 todo 목록을 가져오는 비동기 함수
        // .then() : 비동기 작업이 성공적으로 완료된 후 실행할 코드 지정하는 역할
        getList({page,size}).then(data => {
            // getList 함수가 서버로부터 데이터를 가져오면 실행
            console.log(data)
            setServerData(data)
        })
    }, [page,size,refresh]) // 의존성 배열 page, size의 값이 변경되면 () => 안의 코드들 실행
    return (
        <div className="border-2 border-blue-100 mt-10 mr-2 ml-2">

            <div className="flex flex-wrap mx-auto justify-center p-6">

                {serverData.dtoList.map(todo =>
                <div
                    key={todo.tno}
                    className="w-full min-w-[480px] p-2 m-2 rounded shadow-md"
                    onClick={() => moveToRead(todo.tno)}>
                    <div className="flex ">
                        <div className="font-extrabold text-2xl p-2 w-1/12">
                            {todo.tno}
                        </div>
                        <div className="text-1xl m-1 p-2 w-8/12 font-extrabold">
                            {todo.title}
                        </div>
                        <div className="text-1xl m-1 p-2 w-2/10 font-medium">
                            {todo.dueDate}
                        </div>
                    </div>
                </div>
                )}
            </div>

            <PageComponent serverData={serverData} movePage={moveToList}></PageComponent>
        </div>
    );
}

export default ListComponent;