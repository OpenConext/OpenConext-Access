import {expect, test} from 'vitest'
import {useAppStore} from "../../stores/AppStore";

test("Store outside functional component", () => {
    const csrfTokenFromState = useAppStore.getState().csrfToken;
    expect(csrfTokenFromState).toBeUndefined();

    useAppStore.setState({csrfToken: "test"});

    const updatedCsrfToken = useAppStore.getState().csrfToken;
    expect(updatedCsrfToken).toEqual("test");
//https://mayashavin.com/articles/test-react-hooks-with-vitest
//     const {csrfToken} = useAppStore(state => state);
//     expect(csrfToken).toEqual("test");
});