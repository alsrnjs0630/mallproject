import {useNavigate, useParams} from "react-router-dom";
import ModifyComponent from "../../components/todo/ModifyComponent";

const ModifyPage = () => {

  const {tno} = useParams()
    console.log(tno);  // 올바른 tno 값이 출력되는지 확인

  return(
      <div className={'p-4 w-full bg-white'}>
        <div className={'text-3xl font-extrabold'}>
          Todo Modify Page
        </div>

        <ModifyComponent tno={tno}/>

      </div>
  );
}

export default ModifyPage;