import jwtAxios from "../util/jwtUtil";
import {API_SERVER_HOST} from "./todoApi";

const host = `${API_SERVER_HOST}/api/products`

// 액세스 토큰을 로컬 스토리지 또는 상태에서 가져옵니다.
const getAccessToken = () => {
    // 예를 들어, 로컬 스토리지에서 토큰을 가져오는 경우
    return localStorage.getItem("accessToken") || "";
}

export const postAdd = async (product) => {
    const header = {headers: {"Content-Type": "multipart/form-data"}}
    const res = await jwtAxios.post(`${host}/`, product, header)
    return res.data
}

// 서버에서 상품 목록을 가져와서 반환
export const getList = async (pageParam) => {
    const {page, size} = pageParam
    const res = await jwtAxios.get(`${host}/list`, {params: {page: page, size: size}})
    console.log('API Response:', res.data); // 응답 데이터 확인
    console.log("Access Token:", getAccessToken());
    return res.data
}

export const getOne = async (tno) => {
    const res = await jwtAxios.get(`${host}/${tno}`)
    return res.data
}

export const putOne = async (pno, product) => {
    const header = {headers: {"Content-Type": "multipart/form-data"}}
    const res = await jwtAxios.put(`${host}/${pno}`, product, header)
    return res.data
}

export const deleteOne = async (pno) => {
    const res = await jwtAxios.delete(`${host}/${pno}`)
    return res.data
}