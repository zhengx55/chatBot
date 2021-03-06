import React, {Component} from 'react';
import axios from 'axios';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import './Feedback.css';
import Container from '@material-ui/core/Container';
import Hidden from "./Menu/Hidden/Hidden";
import Background from "./Menu/Background/Background";
import Pullbar from "./Menu/Pullbar/Pullbar";
import {ToastContainer, toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Cookies from 'js-cookie';


class Feedback extends Component {

    constructor(props) {
        super(props);
        this.state = {
            hiddenStatus: false,
            name: '',
            feedback: ''
        };

        this.handlenameChange = this.handlenameChange.bind(this);
        this.handlefeedbackChange = this.handlefeedbackChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    pullToggle = () => {
        this.setState((last) => {
            return {hiddenStatus: last};
        });
    };

    closeMenu = () => {
        this.setState({hiddenStatus: false});
    };

    handlenameChange(event) {
        this.setState({name: event.target.value});
    }

    handlefeedbackChange(event) {
        this.setState({feedback: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const feedback = {message: this.state.feedback};
        let headers = {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + Cookies.get('token')
        };
        axios.post('http://localhost:8000/feedback', feedback, {headers: headers})
            .then(response => {
                if (response.status === 200) {
                    toast.success('Feedback successfully sent!', {autoClose: 1000});
                }

            })
            .catch(error => {
                console.log(error);
                toast.error(error, {autoClose: 1000});
            });

        this.setState({name: '', feedback: ''});
    }

    render() {
        let hidden;
        let close;
        if (this.state.hiddenStatus) {
            hidden = <Hidden/>;
            close = <Background click={this.closeMenu}/>;
        }
        if (Cookies.get('username')) {
            return (
                <div className="feedbackPage">
                    <Pullbar clickHandler={this.pullToggle}/>
                    {hidden}
                    {close}
                    <div className="form-group">
                        <ToastContainer/>
                    </div>

                    <h1>Leave Us Feedback</h1>
                    <Container maxWidth="xs">
                        <form className="feedbackForm" onSubmit={this.handleSubmit}>
                            <TextField
                                id="filled-multiline-static"
                                label="Your feedback"
                                multiline
                                rows="10"
                                margin="normal"
                                variant="filled"
                                value={this.state.feedback} onChange={this.handlefeedbackChange}
                            />
                            <Button variant="contained" color="secondary" type="submit">
                                Submit
                            </Button>
                        </form>
                    </Container>
                </div>
            );
        } else {
            return (<div className="accessError">
                <h2>401 Access Denied</h2>
            </div>);
        }
    }
}

export default Feedback;
