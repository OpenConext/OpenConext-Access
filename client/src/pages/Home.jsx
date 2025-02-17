import {useAppStore} from "../stores/AppStore";
import I18n from "../locale/I18n";
import React, {useEffect, useState} from "react";

import Tabs from "../components/Tabs";
import "./Home.scss";
import {UnitHeader} from "../components/UnitHeader";
import {useNavigate, useParams} from "react-router-dom";
import {Page} from "../components/Page";
import {Loader} from "@surfnet/sds";
import VerifyWizard from "../tabs/VerifyWizard.jsx";

export const Home = () => {

    const {tab = "verify"} = useParams();
    const [currentTab, setCurrentTab] = useState(tab);
    const [tabs, setTabs] = useState([]);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    const user = useAppStore((state) => state.user);

    useEffect(() => {
        if (user) {
            useAppStore.setState({
                breadcrumbPath: [
                    {path: "/home", value: I18n.t("tabs.home")},
                    {value: I18n.t(`tabs.${currentTab}`)}
                ]
            });
        }
        const newTabs = [
            <Page key="verify"
                  name="verify"
                  label={I18n.t("tabs.verify")}>
                <VerifyWizard/>
            </Page>
        ];
        setTabs(newTabs);
        setLoading(false);
    }, [currentTab, user]);// eslint-disable-line react-hooks/exhaustive-deps

    const tabChanged = (name) => {
        setCurrentTab(name);
        navigate(`/home/${name}`);
    }

    if (loading) {
        return <Loader/>
    }
    return (
        <div className="home">
            <div className="mod-home-container">
                <UnitHeader obj={({name: I18n.t("landing.header.title")})}>
                    <p>{I18n.t("landing.header.subTitle")}</p>
                </UnitHeader>
                <Tabs activeTab={currentTab}
                      tabChanged={tabChanged}>
                    {tabs}
                </Tabs>
            </div>
        </div>
    );
}
