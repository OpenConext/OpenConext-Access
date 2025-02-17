import "./Confirmation.scss";
import React from "react";
import I18n from "../locale/I18n";
import {Button, ButtonType, Toaster, ToasterType} from "@surfnet/sds";
import HighFive from "../icons/undraw_High_five.svg";
import {useAppStore} from "../stores/AppStore.js";

const Confirmation = ({restart}) => {

    const {controlCode} = useAppStore(state => state);

    return (
        <div className="confirmation">
            <h4>{I18n.t("confirmation.header")}</h4>
            <Toaster toasterType={ToasterType.Success}
                     message={I18n.t("confirmation.info", {controlCode})}/>
            <div className={"img-container"}>
                <HighFive/>
            </div>
            <div className={"button-container"}>
                <Button txt={I18n.t("confirmation.restart")}
                        type={ButtonType.GhostDark}
                        onClick={() => restart()}
                />
            </div>
        </div>
    );

};

export default Confirmation;