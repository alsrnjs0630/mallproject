// root.js : createBrowserRouter()를 통해 어떤 경로에는 어떤 컴포넌트를 보여줄 것인지를 결정하는 역할 
import { lazy, Suspense } from "react";
import todoRouter from "./todoRouter";
import productRouter from "./productRouter";

const { createBrowserRouter } = require("react-router-dom");

const Loading = <div>Loading....</div>
const Main = lazy(() => import("../pages/MainPage"))
const About = lazy(() => import("../pages/AboutPage"))
const TodoIndex = lazy(() => import("../pages/todo/IndexPage"))
// const TodoList = lazy(() => import("../pages/todo/ListPage")) todoRouter로 호출
const ProductsIndex = lazy(() => import("../pages/products/IndexPage"))

// 라우팅 설정 -> 어떤 경로에서 어떤 컴포넌트를 렌더링 할지 결정
const root = createBrowserRouter ([
    // createBrowserRouter() : 각 경로와 경로에서 렌더링할 컴포넌트를 매핑하여 브라우저 라우팅 설정
    // 'lazy' : 컴포넌트를 동적으로 가져오는 기능을 제공 -> 컴포넌트를 필요할 때 비동기로 불러와 초기 로딩 시간을 줄임.
    // 'Suspense' : lazy로 로드되는 컴포넌트를 감싸는 컴포넌트. 컴포넌트가 로드되는 동안 보여줄 대체 UI 제공 -> 현재는 Loading으로 대체
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
  },
  {
    path: "products",
    element: <Suspense fallback={Loading}><ProductsIndex/></Suspense>,
    children: productRouter()
  }
])

export default root;