// 하나의 라우팅 설정에 childeren 속성을 이용해서 중첩적인 라우팅 설정을 적용할 때 페이지가 많아지면 root.js 파일이 너무 복잡해짐

// 이럴 대는 별도의 함수에서 children 속성값에 해당하는 설정을 반환하는 방식이 알아보기 쉽다

// '/todo/' 하위의 설정들을 반환하도록 함수를 수정

import { Suspense, lazy } from "react";
import { Navigate } from "react-router-dom";

const Loading = <div>Loading....</div>
const TodoList = lazy(() => import("../pages/todo/ListPage"))
const TodoRead = lazy(() => import("../pages/todo/ReadPage"))
const TodoAdd = lazy(() => import('../pages/todo/AddPage'))
const TodoModify = lazy(() => import("../pages/todo/ModifyPage"))

const todoRouter = () => {

  return [
    {
      path: "list",
      element: <Suspense fallback={Loading}><TodoList/></Suspense>
    },
    {
      // 메뉴에서 Todo버튼을 누르거나 직접 /todo/ 경로로 접근할 경우 todo/list로 이동
      path: "",
      element: <Navigate replace to="list"/>
    },
    {
      path:"read/:tno",
      element: <Suspense fallback={Loading}><TodoRead/></Suspense>
    },
    {
      path: "add",
      element: <Suspense fallback={Loading}><TodoAdd/></Suspense>
    },
    {
      path: "modify/:tno",
      element: <Suspense fallback={Loading}><TodoModify/></Suspense>
    }
  ]
}

export default todoRouter;

