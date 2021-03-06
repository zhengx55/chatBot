import React from 'react';
import './Toggle.css';

const toggle = (props) => (
    <button className="toggle_botton" onClick={props.click}>
        <div className="line" />
        <div className="line" />
        <div className="line" />
    </button>
);

export default toggle;