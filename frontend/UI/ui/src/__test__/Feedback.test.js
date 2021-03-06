import React from 'react';
import { shallow ,mount} from 'enzyme';
import Feedback from '../components/Feedback';
import TextField from "@material-ui/core/TextField";


describe("[UNIT] Testing the Feedback component", () => {
    let wrapper;

    describe("Component validation", () => {

        wrapper = shallow(<Feedback />);

        it('Feedback renders without crashing', () => {
            expect(wrapper.find("h2").text()).toEqual("401 Access Denied");
        });

       /* it('correct input feedback textfield change', () => {
            wrapper.find(TextField).simulate('change', {target: {value: 'Good design'}});
            expect(wrapper.state('feedback')).toEqual('Good design');
        });*/

    });

});
