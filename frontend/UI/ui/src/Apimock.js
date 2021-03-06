import React,{useState,useEffect} from "react";
import axios from "axios";

export default function Apimock(props){
    const [data, setData]=useState(null);

    useEffect(
        ()=>{
            let mounted=true;

            const loadData=async () => {
                const response =await axios.get(props.url);
                if(mounted){
                    setData(response.data);
                }
            };
            loadData();

            return () =>{
                mounted=false;
            };
        },
        [props.url]
    );


    if(!data){
        return <div data-testid="waiting">Please wait a sec!</div>;
    }

    return <div data-testid="transmiting">{data.response}</div>;
}
