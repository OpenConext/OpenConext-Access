import React from "react";
import {Button, ButtonSize, ButtonType} from "@surfnet/sds";
import './Login.scss';
import I18n from "../locale/I18n";
import DOMPurify from "dompurify";
import Frontdesk from "../icons/frontdesk.svg";
import {LandingInfo} from "../components/LandingInfo";

export const Login = () => {

    const doLogin = () => {
        const path = window.location.origin;
        window.location.href = `${path}/Shibboleth.sso/Login?target=/`;
    }

    return (
        <div className="top-container">
            <div className="mod-login-container">
                <div className="mod-login">
                    <div className="header-left">
                        <h1 className={"header-title"}
                            dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(I18n.t("landing.header.title"))}}/>
                        <Button onClick={() => doLogin()}
                                txt={I18n.t("landing.header.login")}
                                type={ButtonType.Primary}
                                size={ButtonSize.Full}/>
                        <p className={"sup"}
                           dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(I18n.t("landing.header.sup"))}}/>
                    </div>
                    <div className="header-right">
                        <Frontdesk/>
                    </div>
                </div>
            </div>
            <LandingInfo/>
        </div>
    );

}