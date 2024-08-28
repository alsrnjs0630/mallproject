import {useEffect} from "react";
import {useSearchParams} from "react-router-dom";
import {getAccessToken, getMemberWithAccessToken} from "../../api/kakaoApi";

const KakaoRedirectPage = () => {
    const [searchParams] = useSearchParams()
    const authCode = searchParams.get("code")

    useEffect(() => {
        getAccessToken(authCode).then(accessToken => {
            console.log(accessToken)
            getMemberWithAccessToken(accessToken).then(memberInfo => {
                console.log("------------------")
                console.log(memberInfo)
            })
        })
    }, [authCode]);

    return (
        <div>
            <div>Kakao Login Redirect</div>
            <div>{authCode}</div>
        </div>
    )
}

export default KakaoRedirectPage;