import React, {useState} from "react";
import "./UnitHeader.scss";
import Frontdesk from "../icons/frontdesk.svg";

export const UnitHeader = ({obj, children}) => {

    const [isAnimating, setIsAnimating] = useState(false);

    const toggleAnimation = () => {
        setIsAnimating(true);
        setTimeout(() => setIsAnimating(false), 2250);
    }

    return (
        <div className="unit-header-container">
            <div className="unit-header">
                <div className={`image ${obj.style || ""} ${isAnimating ? "animated" : ""}`}
                     onClick={toggleAnimation}>
                    <Frontdesk/>
                </div>
                <div className="obj-name">
                    <h1>{obj.name}</h1>
                    {obj.description}
                    {children}
                </div>
            </div>
        </div>
    )
}
