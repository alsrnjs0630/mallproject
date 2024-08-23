// root.js : createBrowserRouter()를 통해 어떤 경로에는 어떤 컴포넌트를 보여줄 것인지를 결정하는 역할 
import { lazy, Suspense } from "react";
import todoRouter from "./todoRouter";

const { createBrowserRouter } = require("react-router-dom");

const Loading = <div>Loading....</div>
const Main = lazy(() => import("../pages/MainPage"))
const About = lazy(() => import("../pages/AboutPage"))
const TodoIndex = lazy(() => import("../pages/todo/IndexPage"))
// const TodoList = lazy(() => import("../pages/todo/ListPage")) todoRouter로 호출

const root = createBrowserRouter ([
  {
    path: "",
    element: <Suspense fallback={Loading}><Main/></Suspense>
  },
  {
    path: "about",
    element: <Suspense fallback={Loading}><About/></Suspense>
  },
  {
    path:"todo",
    element: <Suspense fallback={Loading}><TodoIndex/></Suspense>,
    children: todoRouter()
  }
])

export default root;