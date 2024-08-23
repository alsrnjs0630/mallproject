import { useState } from "react";
import { createSearchParams, useNavigate, useSearchParams } from "react-router-dom";

// 커스텀 훅 파일명은 use~ 형태

// 주어진 파라미터(param)을 숫자로 변환하는 함수. 파라미터가 존재하지 않으면 기본값(defaultValue) 반환
const getNum = (param, defaultValue) => {

    if(!param){
        return defaultValue
    }

    return parseInt(param)
}

// 커스텀 훅 useCustomMove
// 페이지 간 이동을 관리, 쿼리 스트링을 통해 현재 페이지와 항목 수를 다룸
const useCustomMove = () => {

    // 페이지 이동을 위한 훅
    const navigate = useNavigate()

    const [refresh, setRefresh] = useState(false)
    // url의 쿼리 스트링을 읽고 제어할 수 있게 해주는 훅 page와 size의 파라미터를 가져옴
    const [queryParams] = useSearchParams()

    // 현재 URL의 쿼리 스트링에서 page와 size 값 추출. 기본값 1과 10
    const page = getNum(queryParams.get('page'), 1)
    const size = getNum(queryParams.get('size'), 10)
    // page와 size를 기본 쿼리스트링으로 생성 => 다른 페이지로 이동할 때 쿼리스트링 유지에 사용
    const queryDefault = createSearchParams({page,size}).toString() // 새로 추가

    // 다른 페이지로 이동하는 함수. 주어진 pageParam을 사용하여 쿼리스트링 생성, 기본값 queryDefault 사용
    const moveToList = (pageParam) => {

        let queryStr = ""

        if(pageParam) {
            const pageNum = getNum(pageParam.page, page)
            const sizeNum = getNum(pageParam.size, size)

            queryStr = createSearchParams({page:pageNum, size:sizeNum}).toString()
        }
        else {
            queryStr = queryDefault
        }

        // 새로운 경로로 페이지 이동
        navigate({
            pathname : `../list`,
            search:queryStr
        })

        // 상태를 변경하여 컴포넌트를 강제로 다시 렌더링
        setRefresh(!refresh) //추가

    }

    // 수정 페이지로 이동
    const moveToModify = (num) => {
        console.log(queryDefault)

        navigate({
            pathname: `../modify/${num}`,
            search: queryDefault // 수정시에 기존의 쿼리 스트링 유지를 위해
        })
    }

    // 조회 페이지로 이동
    const moveToRead = (num) => {
        console.log(queryDefault)
        navigate({
            pathname: `../read/${num}`,
            search: queryDefault
        })
    }
    // 메서드들을 반환하여 다른 컴포넌트에서 이 훅을 사용하여 페이지 이동이 가능하게함
    return {moveToList, moveToModify, moveToRead, page, size, refresh} // moveToModify 추가
}

export default useCustomMove