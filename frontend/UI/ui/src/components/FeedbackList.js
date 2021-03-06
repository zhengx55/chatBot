import React, {Component} from 'react';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import "./FeedbackList.css"
import Cookies from "js-cookie";
import axios from "axios";
import {toast} from "react-toastify";
import Page from "./PageFeedbackList";


export default class ControlledExpansionPanels extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
      feedbackmsg:[],
      latestFeedback:[],
      pageOfItems: []

    };

    this.changeHandler = this.changeHandler.bind(this);
    this.onChangePage = this.onChangePage.bind(this);
  }

  onChangePage(pageOfItems) {
    // update state with new page of items
    this.setState({ pageOfItems: pageOfItems });
  }

  changeHandler = panel => (back, isExpanded) => {
    this.setState({expanded: isExpanded ? panel : false});
  };

  componentDidMount() {
    this.showFeedbacklist();

  }

  showFeedbacklist = async () => {

    let headers = {
      'Authorization': "Bearer " + Cookies.get('token')
    };

    try {
      await axios.get('http://localhost:8000/api/v1/feedbacks',{ headers: headers })
          .then(res => {
            //console.log(res);

            res.data.feedbacks.map(feedback=>{
              this.setState((prevState) => ({ feedbackmsg: prevState.feedbackmsg.concat(feedback.message)}));
              return null;
            });

            this.setState({latestFeedback:this.state.feedbackmsg.reverse()});

          });

    } catch (err) {
      toast.error(err, { autoClose: 1000 });
    }
  };

  render() {

    const {expanded,latestFeedback,pageOfItems} = this.state;

    return (
        <div className="feedback_list">
        {
          pageOfItems.length ?
              pageOfItems.map((feedback, i) =>
                  <ExpansionPanel key={i} expanded={expanded === i} onChange={this.changeHandler(i)}>
                    <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>} id={i}>
                      <Typography className="User_Feedback">User's feedback</Typography>
                    </ExpansionPanelSummary>
                    <ExpansionPanelDetails>
                      <Typography>
                        {feedback}
                      </Typography>
                    </ExpansionPanelDetails>
                  </ExpansionPanel>
              ) :
              <p>No feedbacks yet!</p>
        }
        <Page items={latestFeedback} onChangePage={this.onChangePage} />
        </div>
    );
  }
}
