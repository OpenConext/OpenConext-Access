import "./VerifyWizard.scss";
import React, {useState} from "react";
import Verification from "./Verification.jsx";
import Control from "./Control.jsx";
import Confirmation from "./Confirmation.jsx";

const VerifyWizard = () => {

    const [step, setStep] = useState(1);

    if (step === 1) {
        return <Verification proceed={() => setStep(2)}/>;
    }
    if (step === 2) {
        return <Control restart={() => setStep(1)} proceed={() => setStep(3)}/>;
    }
    if (step === 3) {
        return <Confirmation restart={() => setStep(1)} />;
    }
};

export default VerifyWizard;