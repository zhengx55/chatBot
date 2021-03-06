import React from 'react';
import { shallow } from 'enzyme';
import Signup from '../components/Signup';
import TextField from "@material-ui/core/TextField";
import Typography from '@material-ui/core/Typography';

describe("[UNIT] Testing the Signup component", () => {
    let wrapper;

    describe("Component validation", () => {

        wrapper = shallow(<Signup/>)

        it('Signup renders without crashing', () => {
            expect(wrapper.find(Typography).text()).toEqual("Sign up")
        });

        it('correct input username check', () => {
            wrapper.find(TextField).first().simulate('change', {target: {name: 'username', value: 'kyle'}});
            expect(wrapper.state('textusername')).toEqual('kyle');
        });

        it('correct input password check', () => {
            wrapper.find(TextField).at(1).simulate('change', {target: {name: 'password', value: 'xyz'}});
            expect(wrapper.state('textpassword')).toEqual('xyz');
        });

        it('correct input password confirmation check', () => {
            wrapper.find(TextField).last().simulate('change', {target: {name: 'confirm_password', value: 'xyz'}});
            expect(wrapper.state('textconfirmpassword')).toEqual('xyz');
        });
    });

});
