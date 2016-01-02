var IndexNoticeMessage = React.createClass({
    render: function () {
        return (
            <div id="notice-message" className="alert alert-danger" role="alert">{this.props.msg}</div>
        );
    }
});

var IndexLogin = React.createClass({
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
            <input id="input-username" type="text" className="form-control" placeholder="半角英数字" name="login"
                   value={this.state.login} onChange={this.changeText}/>
        );
    }
});

var IndexPassword = React.createClass({
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
            <input type="password" className="form-control" id="input-password" name="password"
                   placeholder="半角英数字・記号（２文字以上）" value={this.state.password} onChange={this.changeText}/>
        );
    }
});
