var IndexNoticeMessage = React.createClass({displayName: "IndexNoticeMessage",
    render: function () {
        return (
            React.createElement("div", {id: "notice-message", className: "alert alert-danger", role: "alert"}, this.props.msg)
        );
    }
});

var IndexLogin = React.createClass({displayName: "IndexLogin",
    getInitialState: function () {
        return {
            login: this.props.login
        };
    },
    changeText: function (e) {
        this.setState({login: e.target.value});
    },
    render: function () {
        return (
            React.createElement("input", {id: "input-username", type: "text", className: "form-control", placeholder: "半角英数字", name: "login", 
                   value: this.state.login, onChange: this.changeText})
        );
    }
});

var IndexPassword = React.createClass({displayName: "IndexPassword",
    getInitialState: function () {
        return {
            password: this.props.password
        };
    },
    changeText: function (e) {
        this.setState({password: e.target.value});
    },
    render: function () {
        return (
            React.createElement("input", {type: "password", className: "form-control", id: "input-password", name: "password", 
                   placeholder: "半角英数字・記号（２文字以上）", value: this.state.password, onChange: this.changeText})
        );
    }
});
