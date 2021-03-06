import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import './Login.css';
import Cookies from 'js-cookie';
import Pullbar from "./Menu/Pullbar/Pullbar";

class Login extends Component {

    constructor(props) {

        super(props);
        this.state = {
            errorMsg: '',
            txtusername: '',
            txtpassword: '',
            txtusernameError: '',
            txtpasswordError: ''
        };

        this.handleusernameChange = this.handleusernameChange.bind(this);
        this.handlepasswordChange = this.handlepasswordChange.bind(this);
        this.signInSubmit = this.signInSubmit.bind(this);
    }

    componentDidMount() {
        if (Cookies.get('token')) {
            const location = {
                pathname: '/Chatbot'
            };
            this.props.history.push(location);
        } else {
            const location = {
                pathname: '/Login'
            };
            this.props.history.push(location);
        }

    }

    handleusernameChange(event) {
        this.setState({txtusername: event.target.value});
    }

    handlepasswordChange(event) {
        this.setState({txtpassword: event.target.value});
    }

    validate = () => {
        let txtusernameError = '';
        let txtpasswordError = '';

        if (!this.state.txtusername) {
            txtusernameError = "username can not be blank";
        }

        if (!this.state.txtpassword) {
            txtpasswordError = "invalid password";
        }

        if (txtusernameError || txtpasswordError) {
            this.setState({txtusernameError: txtusernameError, txtpasswordError: txtpasswordError});
            return false;
        }
        return true;

    };

    // the handler function for login
    signInSubmit(event) {
        event.preventDefault();
        const isValid = this.validate();
        // get all user inputs
        if (isValid) {
            const {txtusername, txtpassword} = this.state;
            // send username and password to the backend
            this.sendSignIn(txtusername, txtpassword);
        }
    };

    sendSignIn = async (username, password) => {

        const response = await fetch('http://localhost:8000/oauth/token', {
            method: 'POST',
            headers: {
                "Authorization": "Basic " + btoa("chatbot:L80eUZjHnafVOG5TWRenSGfiMkPL2j03"),
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body:
                new URLSearchParams({
                    'grant_type': 'password',
                    'username': username,
                    'password': password

                })
        });
        if (response.status !== 200) {
            if (response.status === 401) this.setState({
                errorMsg: "Username or password not correct..."
            });
            if (response.status === 400) this.setState({
                errorMsg: "Username or password not correct..."
            });
            if (response.status === 500) this.setState({
                errorMsg: "Server side error, please try again later..."
            });
        } else {
            const body = await response.json();
            if (body) {
                //console.log(body.access_token);
                this.setState({
                    errorMsg: ''
                });

                Cookies.set('token', body.access_token);
                Cookies.set('username', username);

                const responses = await fetch('http://localhost:8000/users/role', {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body:
                        JSON.stringify({
                            'username': username,
                        })
                });

                const role_body = await responses.json();
                if (role_body) {
                    // console.log(role_body);
                    Cookies.set('role', role_body.role);
                }


                sessionStorage.setItem("chatArray", JSON.stringify([{from: "chatbot", msg: "hi"}]));


                // redirect to Chat page
                const location = {
                    pathname: '/Chatbot',
                };
                this.props.history.push(location);
            }
        }

    };


    render() {
        return (
            <div>
                <Pullbar/>
                <Container maxWidth="xs" className="LoginContainer">
                    <CssBaseline/>

                    <div className="login_paper">
                        <p className="loginError">{this.state.errorMsg}</p>
                        <Typography component="h1" variant="h5">
                            Log in
                        </Typography>
                        <form className="form" noValidate onSubmit={this.signInSubmit}>
                            <TextField
                                variant="outlined"
                                margin="normal"
                                required
                                fullWidth
                                id="username"
                                label="Username"
                                name="username"
                                value={this.state.txtusername}
                                onChange={this.handleusernameChange}
                                autoFocus
                            />
                            <div className="usernameError">{this.state.txtusernameError}</div>
                            <TextField
                                variant="outlined"
                                margin="normal"
                                required
                                fullWidth
                                name="password"
                                label="Password"
                                value={this.state.txtpassword}
                                onChange={this.handlepasswordChange}
                                type="password"
                                id="password"
                                autoComplete="current-password"
                            />
                            <div className="passwordError">{this.state.txtpasswordError}</div>
                            <div className="LoginButtons">
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                    className="LoginSubmit"
                                >
                                    Log In
                                </Button>

                            </div>
                            <Grid container>
                                <Grid item>
                                    <Link href="/Signup" variant="body2">
                                        {"Don't have an account? Sign Up"}
                                    </Link>
                                </Grid>
                            </Grid>
                        </form>
                    </div>
                </Container>
            </div>
        );
    }
}

export default Login;
