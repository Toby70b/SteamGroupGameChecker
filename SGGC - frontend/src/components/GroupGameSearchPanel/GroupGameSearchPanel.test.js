import React from 'react';
import {mount} from 'enzyme';
import GroupGameSearchPanel from "../GroupGameSearchPanel/GroupGameSearchPanel";

test('Add steam Ids works as expected', () => {
    const component = mount(<GroupGameSearchPanel errorMessage={""} dataSource={{}}/>);
    let idInput = component.find('.ant-input');
    expect(idInput.getDOMNode()).toBeVisible();
    const form =(component.find('#steamIdForm').first());
    form.invoke('onFinish')({steamId:'76561198045206229'});
    //TEST things appear



});


/*
test('banner is visible on startup and links are correct', () => {
    const EXPECTED_GITHUB_URL = "https://github.com/Toby70b/SteamGroupGameChecker";
    const EXPECTED_LINKED_IN_URL = "https://www.linkedin.com/in/tobias-peel/";

    const component = shallow(<GroupGameSearchPage/>);
    const githubIcon = component.find('#githubLink').first();
    const linkedinIcon = component.find('#linkedinLink').first();

    expect(githubIcon.exists()).toEqual(true);
    expect(linkedinIcon.exists()).toEqual(true);

    expect(githubIcon.props().href).toEqual(EXPECTED_GITHUB_URL);
    expect(linkedinIcon.props().href).toEqual(EXPECTED_LINKED_IN_URL);
});

test('the search panel is visible on startup', () => {
    const component = mount(<GroupGameSearchPage/>);
    const searchPanel = component.find(GroupGameSearchPanel);
    expect(searchPanel.getDOMNode()).toBeVisible();
});

test('search results panel is not visible on startup', () => {
    const component = shallow(<GroupGameSearchPage/>);
    expect(component.find(GroupGameSearchResultsPanel).exists()).toEqual(false);
});
*/
