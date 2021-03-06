import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import './Signup.css';

class Signup extends Component {

    constructor(props) {

        super(props);
        this.state = {
            errorMsg: '',
            textusername:'',
            textpassword:'',
            textconfirmpassword:'',
            textusernameError:'',
            textpasswordError:'',
            textconfirmpasswordError:''
        };

        this.handleusernameChange = this.handleusernameChange.bind(this);
        this.handlepasswordChange = this.handlepasswordChange.bind(this);
        this.handleconfirmpasswordChange = this.handleconfirmpasswordChange.bind(this);
        this.signUpSubmit = this.signUpSubmit.bind(this);
    }

    handleusernameChange(event) {
        this.setState({textusername: event.target.value});
    }

    handlepasswordChange(event) {
        this.setState({textpassword: event.target.value});
    }

    handleconfirmpasswordChange(event) {
        this.setState({textconfirmpassword: event.target.value});
    }

    validate = () => {
        let textusernameError = '';
        let textpasswordError = '';
        let textconfirmpasswordError='';

        if (!this.state.textusername) {
            textusernameError = "username can not be blank";
        }

        if (!this.state.textpassword) {
            textpasswordError = "invalid password";
        }

        if (this.state.textconfirmpassword !== this.state.textpassword) {
            textconfirmpasswordError = "password confirmation does not match your password";
        }

        if (this.state.textusername.length < 4) {
            textusernameError = "username should be at least 4 characters";
        }

        if (this.state.textpassword.length < 4) {
            textpasswordError = "password should be at least 4 characters";
        }

        if (textusernameError || textpasswordError || textconfirmpasswordError) {
            this.setState({textusernameError: textusernameError, textpasswordError: textpasswordError, textconfirmpasswordError: textconfirmpasswordError});
            return false;
        }
        return true;

    };

    signUpSubmit(event) {
        event.preventDefault();
        this.setState({
            errorMsg: ''
        });
        // get all user inputs
        const isValid = this.validate();
        if (isValid) {
            const {textusername,textpassword} = this.state;
            // check if the password matches passwordConfirmation
            this.sendSignUp(textusername, textpassword);
        }
    };

    sendSignUp = async (username, password) => {

        const response = await fetch('http://localhost:8000/users/signup', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
            })
        });
        if (response.status !== 200) {
            if (response.status === 400) this.setState({
                errorMsg: "Username or password must be at least 4 characters..."
            });
            if (response.status === 409) this.setState({
                errorMsg: "Username has been taken..."
            });
            if (response.status === 500) this.setState({
                errorMsg: "Server side error, please try again later..."
            });
        } else {
            const body = await response.json();
            if (body) {
                console.log(body);
                if(body.status ==="username already existed!!!"){
                    this.setState({
                        errorMsg: body.status
                    });
                } else {
                    this.setState({
                        errorMsg: ''
                    });

                    this.props.history.push('/Login');
                }
            }
        }

    };


    render() {
        return (
            <Container maxWidth="xs" className="SignupContainer">
                <CssBaseline/>
                <div className="signup_paper">
                    <p className="error_msg">{ this.state.errorMsg }</p>
                    <Typography component="h1" variant="h5">
                        Sign up
                    </Typography>
                    <form className="form" noValidate onSubmit={this.signUpSubmit}>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            id="username"
                            label="Username"
                            value={this.state.textusername}
                            onChange={this.handleusernameChange}
                            name="username"
                            autoFocus
                        />
                        <div className="usernameError">{this.state.textusernameError}</div>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            value={this.state.textpassword}
                            onChange={this.handlepasswordChange}
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                        <div className="passwordError">{this.state.textpasswordError}</div>
                        <TextField
                            variant="outlined"
                            margin="normal"
                            required
                            fullWidth
                            name="confirm_password"
                            label="Confirm Password"
                            value={this.state.textconfirmpassword}
                            onChange={this.handleconfirmpasswordChange}
                            type="password"
                            id="confirm_password"
                            autoComplete="current-password"
                        />
                        <div className="passwordError">{this.state.textconfirmpasswordError}</div>
                        <div className="SignupButtons">
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                color="primary"
                                className="SignupSubmit"
                            >
                                Sign up
                            </Button>
                        </div>
                        <Grid container>
                            <Grid item>
                                <Link href="/Login" variant="body2">
                                    {"Already have an account? Log in"}
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>
            </Container>

        );
    }
}

export default Signup;
