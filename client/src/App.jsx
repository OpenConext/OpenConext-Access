import React, {useEffect, useState} from 'react'
import {Loader} from "@surfnet/sds";
import './App.scss';
import {Navigate, Route, Routes, useNavigate} from "react-router-dom";
import {me} from "./api/index.js";
import {useAppStore} from "./stores/AppStore.js";
import {Flash} from "./components/Flash.jsx";
import {Header} from "./components/Header.jsx";
import {BreadCrumb} from "./components/BreadCrumb.jsx";
import NotFound from "./pages/NotFound.jsx";
import RefreshRoute from "./pages/RefreshRoute.jsx";
import {Login} from "./pages/Login.jsx";
import {Home} from "./pages/Home.jsx";
import {Footer} from "./components/Footer.jsx";

const App = () => {

    const [loading, setLoading] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        config()
            .then(res => {
                useAppStore.setState(() => ({user: res}));
                setLoading(false);
                if (res.serviceDeskMember) {
                    setIsAuthenticated(true);
                    navigate("/home");
                } else {
                    navigate("/404");
                }
            }).catch(() => {
                setLoading(false);
                navigate("/login");
        });

    }, []);

    if (loading) {
        return <Loader/>
    }

    return (
        <div className="access">
            <div className="container">
                <Flash/>
                <Header/>
                {isAuthenticated && <BreadCrumb/>}
                {isAuthenticated &&
                    <Routes>
                        <Route path="/" element={<Navigate replace to="home"/>}/>
                        <Route path="/home/:tab?" element={<Home/>}/>
                        <Route path="/refresh-route/:path" element={<RefreshRoute/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="*" element={<NotFound/>}/>
                    </Routes>}
                {!isAuthenticated &&
                    <Routes>
                        <Route path="/" element={<Navigate replace to="login"/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/*" element={<NotFound/>}/>
                    </Routes>}
            </div>
            {<Footer/>}
        </div>
    );
}

export default App;