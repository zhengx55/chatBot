import React from 'react';
import { shallow } from 'enzyme';
import Login from '../components/Login';
import TextField from "@material-ui/core/TextField";
import Typography from '@material-ui/core/Typography';

describe("[UNIT] Testing the Login component", () => {
    let wrapper;

    describe("Component validation", () => {

        const historyMock = { push: jest.fn() };
        wrapper = shallow(<Login history={historyMock} />)

        it('Login renders without crashing', () => {
            expect(wrapper.find(Typography).text()).toEqual("Log in")
        });

        it('correct input username check', () => {
            wrapper.find(TextField).first().simulate('change', {target: {name: 'username', value: 'kyle'}});
            expect(wrapper.state('txtusername')).toEqual('kyle');
        });

        it('correct input password check', () => {
            wrapper.find(TextField).last().simulate('change', {target: {name: 'password', value: 'xyz'}});
            expect(wrapper.state('txtpassword')).toEqual('xyz');
        });
    });

});
