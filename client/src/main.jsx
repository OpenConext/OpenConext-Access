import {createRoot} from 'react-dom/client'
import App from './App.jsx'
import {BrowserRouter, Route, Routes} from "react-router-dom";
//Do not change the order of @surfnet.sds style imports
import '@surfnet/sds/styles/sds.css';
import '@surfnet/sds/cjs/index.css';
import "react-tooltip/dist/react-tooltip.css";
import './index.scss';
import {StrictMode} from "react";

const root = createRoot(document.getElementById("root"));
root.render(
    <StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/*" element={<App/>}/>
            </Routes>
        </BrowserRouter>
    </StrictMode>
);
