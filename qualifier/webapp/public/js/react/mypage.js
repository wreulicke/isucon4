var MyPageLastLogin = React.createClass({displayName: "MyPageLastLogin",
    render: function () {
        return (
            React.createElement("dl", {className: "dl-horizontal"}, 
                React.createElement("dt", null, "前回ログイン"), 
                React.createElement("dd", {id: "last-logined-at"}, dateFormat(this.props.createdAt)), 
                React.createElement("dt", null, "最終ログインIPアドレス"), 
                React.createElement("dd", {id: "last-logined-ip"}, this.props.ip)
            )
        );
    }
});

var MyPageLogin = React.createClass({displayName: "MyPageLogin",
    render: function () {
        return (
            React.createElement("div", {className: "panel-heading"}, this.props.login)
        );
    }
});
