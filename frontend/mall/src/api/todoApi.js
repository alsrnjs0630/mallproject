import axios from "axios"

// 서버 주소
// API 서버의 기본 URL
export const API_SERVER_HOST = 'http://localhost:8080'

const prefix = `${API_SERVER_HOST}/api/todo` // 최종 기본경로

export const getOne = async (tno) => {
    const res = await axios.get(`${prefix}/${tno}`) // URL로 GET요청을 보냄 get호출이 완료될 때까지 코드 실행 일시중지.

    return res.data // 응답 객체에서 데이터 부분을 추출. 반환 : todo 항목의 데이터
}

export const getList = async(pageParam) => {
    const {page,size} = pageParam // pageParam 객체에서 page 와 size 속성 추출
    const res = await axios.get(`${prefix}/list`, {params: {page:page,size:size }})
    // 요청을 보내는 주소는 'http://localhost:8080/api/todo/list?page={page}&size={size}'와 같다.
    return res.data
}

export const postAdd = async (todoObj) => {
    const res = await axios.post(`${prefix}/` , todoObj)
    return res.data
}

/*
* 'async' : 함수 앞에 붙여서 비동기 함수를 정의. 이 함수는 항상 'Promise'를 반환.
*  비동기 작업이 완료될 때까지 코드 실행을 일시 중지하고, 비동기 작업이 끝난 후 결과를 반환
*/

/*
'await' : 'async' 함수 내에서만 사용 가능
'Promise'가 해결될 때까지 코드 실행을 일시 중지하고 해결된 후 결과 값 반환. 이를 통해 Promise를 사용하는 비동기 작업을 동기적은 방식으로 작성 가능
*/

/*
* 'axios' : 비동기 HTTP 요청을 처리하기 위해 사용되는 라이브러리
* 서버에 요청을 보내고 응답을 받는 데 사용되며, 'Promise'를 반환. 'Promise' 기반이기 때문에 비동기 작업을 다룰 수 있음
* 'axios'를 사용하여 HTTP 요청을 보내면, 요청이 완료될 때까지 기다리는 비동기 작업을 처리할 수 있음.
* */
