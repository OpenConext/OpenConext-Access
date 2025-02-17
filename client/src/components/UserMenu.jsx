import I18n from "../locale/I18n";
import React, {useState} from "react";
import "./UserMenu.scss";
import {useNavigate} from "react-router-dom";
import {stopEvent} from "../utils/Utils";
import {UserInfo} from "@surfnet/sds";
import {useAppStore} from "../stores/AppStore";
import {logout} from "../api";


export const UserMenu = ({user}) => {
    const navigate = useNavigate();

    const [dropDownActive, setDropDownActive] = useState(false);

    const logoutUser = e => {
        stopEvent(e);
        logout().then(() => {
            useAppStore.setState(() => ({breadcrumbPath: [], user: {}, controlCode: {}}));
            navigate("/login");
            setTimeout(() =>
                useAppStore.setState(() => ({user: null, breadcrumbPath: []})), 125);
        });
    }

    const renderMenu = () => {
        return (<>
                <ul>
                    <li>
                        <a href="/logout" onClick={logoutUser}>{I18n.t(`header.links.logout`)}</a>
                    </li>
                </ul>
            </>
        )
    }

    return (
        <div className="user-menu"
             tabIndex={1}
             onBlur={() => setTimeout(() => setDropDownActive(false), 325)}>
            <UserInfo isOpen={dropDownActive}
                      children={renderMenu()}
                      organisationName={I18n.t("serviceDesk.member")}
                      userName={user.name}
                      toggle={() => setDropDownActive(!dropDownActive)}
            />
        </div>
    );


}
