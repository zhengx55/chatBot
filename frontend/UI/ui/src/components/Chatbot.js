import React, { Component } from 'react';
import './Chatbot.css';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Chip from '@material-ui/core/Chip';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import axios from 'axios';
import Cookies from 'js-cookie';
import { If, Then, Else } from 'react-if-elseif-else-render';
import Pullbar from "./Menu/Pullbar/Pullbar";
import Hidden from "./Menu/Hidden/Hidden";
import Background from "./Menu/Background/Background";
import Avatar from '@material-ui/core/Avatar';
import FaceIcon from '@material-ui/icons/Face';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails'

class Chatbot extends Component {

    constructor(props) {
        super(props);
        this.state = {
            hiddenStatus: false,
            textValue: '',
            chatArray: []
        };
        this.clickEvent = this.clickEvent.bind(this);
        this.messagesEndRef = React.createRef();
    }

    pullToggle = () =>{
        this.setState((last) => {
            return{hiddenStatus: last};
        });
    };

    closeMenu =() =>{
        this.setState({hiddenStatus: false});
    };

    componentDidMount() {
        if (!Cookies.get('token')) {
            const location = {
                pathname: '/Login'
            };
            this.props.history.push(location);
        } else {
            if(sessionStorage.getItem('chatArray')!==null) {
                let prechatArray = JSON.parse(sessionStorage.getItem('chatArray'));
                prechatArray.map(chat => {
                    this.setState((prevState) => ({
                        chatArray: prevState.chatArray.concat({
                            from: chat.from,
                            msg: chat.msg
                        })
                    }));
                    return null;
                });
            } else{
                Cookies.remove('token');
                Cookies.remove('username');
                const location = {
                    pathname: '/Login'
                };
                this.props.history.push(location);
            }
        }
    }
    componentDidUpdate(){
      this.scrollToBottom();
    };

    scrollToBottom = () =>{
        this.messagesEndRef.current.scrollIntoView({behavior:"smooth"});
    };
    changeTextValue = e => {
        this.setState({ textValue: e.target.value });
    };

    query = () => {
        const message = { message: this.state.textValue, username: Cookies.get('username') };
        let headers = {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + Cookies.get('token')
        };
        try {

            axios.post("http://localhost:8000/query", message, { headers: headers })
                // upon request is success sent
                .then(res => {
                    // update result in the state.

                    console.log(res);
                    if (res.status === 200) {
                        if(res.data.message){
                            this.setState((prevState) => ({ chatArray: prevState.chatArray.concat({ from: 'chatbot', msg: res.data.message }) }), () => {
                                sessionStorage.setItem("chatArray", JSON.stringify(this.state.chatArray));
                            });
                        }
                        if(res.data.content) {
                            this.setState((prevState) => ({ chatArray: prevState.chatArray.concat({ from: 'chatbot', msg: res.data.content }) }), () => {
                                sessionStorage.setItem("chatArray", JSON.stringify(this.state.chatArray));
                            });
                        }
                    } else {
                        console.log("error");
                    }
                });
        } catch (err) {
            console.log("error");
        }

    };

    clickEvent = () => {
        if (!Cookies.get('token')) {
            const location = {
                pathname: '/Login'
            };
            this.props.history.push(location);
        } else {
            this.setState((prevState) => ({
                chatArray: prevState.chatArray.concat({
                    from: Cookies.get('username'),
                    msg: this.state.textValue
                })
            }), () => {
                sessionStorage.setItem("chatArray", JSON.stringify(this.state.chatArray));
            });

            this.query();

            if (this.state.textValue !== '') {
                this.setState({ textValue: '' });
            }
        }
    };

    render() {
        const { textValue, chatArray } = this.state;
        let hidden;
        let close;
        if(this.state.hiddenStatus){
            hidden = <Hidden />;
            close = <Background click={this.closeMenu}/>;
        }

        return (
            <div className="chat_bot">
                <Pullbar clickHandler={this.pullToggle}/>
                {hidden}
                {close}
                <Paper className="root">
                    <Typography variant="h4" component="h4">
                        Chatbot
                    </Typography>
                    <div className="flex">
                        <div className="chatWindow">
                            {
                                chatArray.length ?
                                    chatArray.map((chat, i) =>

                                        <div key={i}>
                                            <If condition={chat.from === 'chatbot'}>
                                                <Then>
                                                    <div className={chat.from}>
                                                        <div ref={this.messagesEndRef} />
                                                        <Chip avatar={<Avatar src="https://i.pinimg.com/originals/7d/9b/1d/7d9b1d662b28cd365b33a01a3d0288e1.gif" style= {{height: "29px", marginLeft:"1px"}}/>} label={chat.from} variant="outlined" />
                                                            <If condition={chat.msg.length > 100}>
                                                                <Then>
                                                                    <div className="preview">
                                                                        <ExpansionPanel>
                                                                            <ExpansionPanelSummary aria-controls="panel1d-content" id="panel1d-header"  style ={{fontSizeAdjust:"small"}}>
                                                                                <Typography variant='body2' align='left' style={{fontSize:"medium"}}> {"Key Word Summary: (".concat(chat.msg.slice(0,30).concat("...)"))} </Typography>
                                                                            </ExpansionPanelSummary>
                                                                            <ExpansionPanelDetails >
                                                                                <Typography align='left' variant='body1'>
                                                                                    {chat.msg}
                                                                                </Typography>
                                                                            </ExpansionPanelDetails>
                                                                        </ExpansionPanel>
                                                                    </div>
                                                                </Then>
                                                                <Else>
                                                                <div className="message_inbox">
                                                                    <Typography align='left' variant='body1'>{chat.msg}</Typography>
                                                                </div>
                                                                </Else>
                                                            </If>
                                                       
                                                    </div>
                                                </Then>
                                                <Else>
                                                    <div className="user">
                                                        <div className="user_message">
                                                            <Typography align='left' variant='body1'>{chat.msg}</Typography>
                                                        </div>
                                                        <Chip label={chat.from} icon={<FaceIcon />} variant="outlined"  />
                                                    </div>
                                                </Else>
                                            </If>
                                        </div>
                                    ) : null

                            }
                        </div>
                    </div>
                    <div className="flex2">
                        <TextField
                            label="Type message..."
                            className="chatBox"
                            value={textValue}
                            onChange={this.changeTextValue}
                        />       
                        <Button variant="contained" color="primary" className="button" onClick={this.clickEvent}>
                            Send
                        </Button>
                        <div ref={this.messagesEndRef} />
                    </div>
                </Paper>
            </div>
        );
    }
}


export default Chatbot;
