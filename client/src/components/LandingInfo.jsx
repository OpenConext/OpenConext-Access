import React from "react";
import DOMPurify from "dompurify";
import I18n from "../locale/I18n";
import desk from "../icons/chatgpt/desk.webp";
import badge from "../icons/chatgpt/badge.webp";
import screen from "../icons/chatgpt/screen.webp";
import "./LandingInfo.scss";
import {Chip, ChipType} from "@surfnet/sds"

export const LandingInfo = () => {

    const infoBlock = (info, Logo, index) => {
        const reversed = index % 2 === 0 ? "reversed" : "";
        return (
            <div key={index} className={`mod-login info ${reversed}`}>
                <div className="header-left info">
                    <div className={"info-title"}>
                        <h2>{info[0]}</h2>
                        {<div className={"admin-function-container"}>
                            <Chip label={I18n.t(`landing.${info[2] ? "adminFunction" : "studentFunction"}`)}
                                  type={ChipType.Main_400}/>
                        </div>}
                    </div>
                    <p dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(info[1])}}/>
                </div>
                <div className="header-right info">
                    <img src={Logo} alt="logo" className={`${reversed}`}/>
                </div>
            </div>
        );
    }


    const logos = [screen, desk, badge];
    return (
        <div className="mod-login-container bottom">
            <div className="mod-login bottom">
                <h1>{I18n.t("landing.works")}</h1>
                {I18n.translations[I18n.locale].landing.info.map((info, index) =>
                    infoBlock(info, logos[index], index)
                )}
                <div className={"landing-footer"}>
                    <p dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(I18n.t(`landing.footer`))}}/>
                </div>
            </div>
        </div>
    );
}