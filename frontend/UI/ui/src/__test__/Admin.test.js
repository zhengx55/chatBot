import React from 'react';
import { shallow ,mount} from 'enzyme';
import Admin from '../components/Admin';
import TextField from "@material-ui/core/TextField";


describe("[UNIT] Testing the Admin component", () => {
    let wrapper;

    describe("Component validation", () => {

        wrapper = shallow(<Admin/>)

        it('Admin renders without crashing', () => {
           /* expect(wrapper.find("h2").at(0).text()).toEqual("Document Upload");
            expect(wrapper.find("h2").at(1).text()).toEqual("Crawl URL");
            expect(wrapper.find("h2").at(2).text()).toEqual("Uploaded Documents");
            expect(wrapper.find("h2").at(3).text()).toEqual("Feedback box");*/

            expect(wrapper.find("h2").text()).toEqual("401 Access Denied");
        });

        /*it('correct input url textfield change', () => {
            wrapper.find(TextField).simulate('change', {target: {value: 'http://www.google.com'}});
            expect(wrapper.state('selectedurl')).toEqual('http://www.google.com');
        });

        it('correct input selected file', () => {
            const file = new File([], 'instruction.pdf', { type: 'application/pdf' });
            wrapper.find("input").simulate('change', { target: { files: [file] } });
            expect(wrapper.state('selectedFile').name).toEqual("instruction.pdf");
        });*/
    });

});
