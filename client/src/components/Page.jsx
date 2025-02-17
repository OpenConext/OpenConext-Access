import React from "react";
import "./Page.scss";

export const Page = ({children, className = ""}) => {

    return (
        <div className={`page ${className}`}>
            {children}
        </div>
    );
}