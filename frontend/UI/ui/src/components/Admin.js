import React, {Component} from 'react';
import './Admin.css';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import {ToastContainer, toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import TextField from '@material-ui/core/TextField';
import FeedList from './FeedbackList.js'
import isUrl from 'validator/lib/isURL';
import Cookies from 'js-cookie';
import Hidden from "./Menu/Hidden/Hidden";
import Background from "./Menu/Background/Background";
import Pullbar from "./Menu/Pullbar/Pullbar";
import Page from './Page';
import LinearProgress from '@material-ui/core/LinearProgress';


class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            hiddenStatus: false,
            selectedFile: null,
            uploadedFiles: [],
            latestFiles: [],
            selectedurl: null,
            pageOfItems: [],
            isProcessing: false,
            types: ['application/pdf', 'application/msword', 'text/html', 'text/plain', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
        };

        this.onChangePage = this.onChangePage.bind(this);
        this.changeURLValue = this.changeURLValue.bind(this);
        //this.showEvent = this.showEvent.bind(this);

    }

    onChangePage(pageOfItems) {
        // update state with new page of items
        this.setState({pageOfItems: pageOfItems});
    }

    pullToggle = () => {
        this.setState((last) => {
            return {hiddenStatus: last};
        });
    };

    closeMenu = () => {
        this.setState({hiddenStatus: false});
    };

    /* Show button event for Uploaded Documents */
    componentDidMount() {
        this.showEvent();

    }

    showEvent = async () => {

        let headers = {
            'Authorization': "Bearer " + Cookies.get('token')
        };

        try {
            await axios.get('http://localhost:8000/api/v1/documents', {headers: headers})
                .then(res => {
                    res.data.documents.map(doc => {
                        this.setState((prevState) => ({
                            uploadedFiles: prevState.uploadedFiles.concat({
                                name: doc.name,
                                lastmodified: doc.lastModified,
                                lastmodifieduser: doc.lastModifiedUser
                            })
                        }));
                        return null;
                    });

                    this.setState({latestFiles: this.state.uploadedFiles.reverse()});

                });

        } catch (err) {
            toast.error(err, {autoClose: 1000});
        }
    };

    /* Validate the uploading file's type */
    checkMimeType = (event) => {
        //getting file object
        let files = event.target.files[0];
        //define message container
        let err = '';
        // list allow mime type

        if (files !== undefined) {
            if (this.state.types.every(type => files.type !== type)) {
                // create error message and assign to container
                err += files.name + ' is not a supported\n';
            }

        }
        if (err !== '') { // if message not same old that mean has error
            event.target.value = null; // discard selected file
            toast.error(err, {autoClose: 1000});
            return false;
        }
        return true;

    };


    fileSelectedHandler = event => {
        let files = event.target.files[0];

        if (this.checkMimeType(event)) {
            this.setState({
                selectedFile: files
            })
        }
    };

    /* send the uploading file to the backend */
    fileUploadHandler = () => {

        let headers = {
            'Authorization': "Bearer " + Cookies.get('token')
        };
        if (this.state.selectedFile === null) {
            toast.error('please select a valid file', {autoClose: 1000});
        } else {
            this.setState({isProcessing: true});
            const data = new FormData();
            data.append('file', this.state.selectedFile);
            /*******************************************************/
            //also passing username to the backend
            data.append('lastModifiedUser', Cookies.get('username'));

            axios.post("http://localhost:8000/api/v1/documents/files", data, {headers: headers})
                .then(res => { // then print response status
                    console.log(res);
                    toast.success('Upload file success', {autoClose: 1000});

                    this.setState({uploadedFiles: this.state.uploadedFiles.reverse()});
                    if (this.state.uploadedFiles.some(v => (v.name === res.data.filename))) {
                        console.log("duplicate");
                        const uploadedFiles = this.state.uploadedFiles.filter(file => file.name !== res.data.filename);
                        this.setState({uploadedFiles: uploadedFiles});
                    }

                    /**********************************************************************/
                    //need to add response parameters in backend to match username and modified date
                    this.setState((prevState) => ({
                        uploadedFiles: prevState.uploadedFiles.concat({
                            name: res.data.filename,
                            lastmodified: res.data.file.lastModified,
                            lastmodifieduser: res.data.file.lastModifiedUser
                        })
                    }));

                    this.setState({latestFiles: this.state.uploadedFiles.reverse()});
                    this.setState({isProcessing: false});

                })
                .catch(err => {
                    toast.error('Upload file fail', {autoClose: 1000});
                    this.setState({isProcessing: false});
                })
        }
    };

    changeURLValue(e) {
        this.setState({selectedurl: e.target.value});
    };

    /* send the URL to the backend */
    URLUploadHandler = () => {

        let headers = {
            'Authorization': "Bearer " + Cookies.get('token')
        };
        if (this.state.selectedurl === null) {
            toast.error('please give a vaild url', {autoClose: 1000});
        } else {
            this.setState({isProcessing: true});
            if (isUrl(this.state.selectedurl)) {
                console.log(this.state.selectedurl);

                const data = {url: this.state.selectedurl, lastModifiedUser: Cookies.get('username')};
                axios.post("http://localhost:8000/api/v1/documents/urls", data, {headers: headers})
                    .then(res => {
                        console.log(res);
                        toast.success('Upload url success', {autoClose: 1000});

                        this.setState({uploadedFiles: this.state.uploadedFiles.reverse()});
                        if (this.state.uploadedFiles.some(v => (v.name === res.data.filename))) {
                            console.log("duplicate");
                            const uploadedFiles = this.state.uploadedFiles.filter(file => file.name !== res.data.filename);
                            this.setState({uploadedFiles: uploadedFiles});
                        }

                        /**********************************************************************/
                        //need to add response parameters in backend to match username and modified date
                        this.setState((prevState) => ({
                            uploadedFiles: prevState.uploadedFiles.concat({
                                name: res.data.filename,
                                lastmodified: res.data.file.lastModified,
                                lastmodifieduser: res.data.file.lastModifiedUser
                            })
                        }));

                        this.setState({latestFiles: this.state.uploadedFiles.reverse()});
                        this.setState({isProcessing: false});

                    })
                    .catch(err => {
                        toast.error('Upload url fail', {autoClose: 1000});
                        this.setState({isProcessing: false});
                    });
            } else {
                toast.error('Wrong URL format', {autoClose: 1000});
                toast.error('URL format should be "https://www.example.com"', {autoClose: 5000});
                this.setState({isProcessing: false});
            }
        }


    };


    /* send the delete action to the backend */
    deleteHandler = fname => e => {
        console.log(fname.file.name);
        this.setState({isProcessing: true});
        let headers = {
            'Authorization': "Bearer " + Cookies.get('token')
        };
        const data = {filename: fname.file.name};
        axios.delete("http://localhost:8000/api/v1/documents", {headers: headers, data})
            .then(res => {
                console.log(res);
                toast.success(fname.file.name + ' is deleted', {autoClose: 2000});

                const uploadedFiles = this.state.uploadedFiles.filter(file => file.name !== fname.file.name);
                this.setState({uploadedFiles: uploadedFiles});

                const latestFiles = this.state.latestFiles.filter(file => file.name !== fname.file.name);
                this.setState({latestFiles: latestFiles});
                this.setState({isProcessing: false});
            })
            .catch(err => {
                toast.error(fname.file.name + ' deleted fail', {autoClose: 1000});
                //toast.success(fname.file + ' is deleted');
                this.setState({isProcessing: false});
            });

    };


    render() {

        let hidden;
        let close;
        if (this.state.hiddenStatus) {
            hidden = <Hidden/>;
            close = <Background click={this.closeMenu}/>;
        }
        const {latestFiles, pageOfItems} = this.state;
        if (Cookies.get('username')) {

            return (

                <div className="adminPage">
                    <Pullbar clickHandler={this.pullToggle}/>
                    {hidden}
                    {close}
                    <div className="dash_container">
                        {this.state.isProcessing ?
                            <LinearProgress className="progress_bar"/>
                            : null
                        }

                        <div className="admin_container">
                            <div className="form-group">
                                <ToastContainer/>
                            </div>
                            <div className="adminLeft">

                                <div>
                                    <div className="uploadFile admin_component adminPageItem">
                                        <h2>Document Upload</h2>
                                        <input type="file" onChange={this.fileSelectedHandler}/>
                                        <Button variant="contained" component="span" onClick={this.fileUploadHandler}>
                                            Upload
                                        </Button>
                                    </div>

                                    <div className="createURL admin_component adminPageItem">
                                        <h2 className="custom_h2">Crawl URL</h2>
                                        <TextField className="url_fields" type="url" placeholder="Type URL" name="url"
                                                   id="url"
                                                   margin="dense" onChange={this.changeURLValue}/>
                                        <Button className="url_field" variant="contained" component="span"
                                                onClick={this.URLUploadHandler}>
                                            Crawl
                                        </Button>
                                    </div>
                                </div>

                                <div className="indexerView admin_component adminPageItem">
                                    <h2>Uploaded Documents</h2>

                                    <div className="table_group">
                                        <Paper className="classes.paper">

                                            <Table className="classes.table" size="medium">
                                                <TableHead>
                                                    <TableRow>
                                                        <TableCell>Document</TableCell>
                                                        <TableCell align="right">User</TableCell>
                                                        <TableCell align="right">Last_modification</TableCell>
                                                        <TableCell align="right"></TableCell>
                                                    </TableRow>
                                                </TableHead>

                                                {
                                                    pageOfItems.length ?
                                                        pageOfItems.map((file, i) =>
                                                            <TableBody key={i}>
                                                                <TableRow>
                                                                    <TableCell component="th" scope="row">
                                                                        {file.name}
                                                                    </TableCell>
                                                                    <TableCell
                                                                        align="right">{file.lastmodifieduser}</TableCell>
                                                                    <TableCell
                                                                        align="right">{file.lastmodified}</TableCell>
                                                                    <TableCell>
                                                                        <IconButton aria-label="Delete"
                                                                                    onClick={this.deleteHandler({file})}>
                                                                            <DeleteIcon/>
                                                                        </IconButton>
                                                                    </TableCell>
                                                                </TableRow>
                                                            </TableBody>
                                                        ) : null
                                                }
                                            </Table>

                                        </Paper>
                                    </div>
                                    <Page items={latestFiles} onChangePage={this.onChangePage}/>
                                </div>
                            </div>
                            <div className="adminRight">
                                <div className="feedback">
                                    <h2>Feedback box</h2>
                                    <FeedList/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        } else {
            return (<div className="accessError">
                <h2>401 Access Denied</h2>
            </div>);
        }
    }
}

export default Admin;
