var MyPageLastLogin = React.createClass({
    render: function () {
        return (
            <dl className="dl-horizontal">
                <dt>前回ログイン</dt>
                <dd id="last-logined-at">{dateFormat(this.props.createdAt)}</dd>
                <dt>最終ログインIPアドレス</dt>
                <dd id="last-logined-ip">{this.props.ip}</dd>
            </dl>
        );
    }
});

var MyPageLogin = React.createClass({
    render: function () {
        return (
            <div className="panel-heading">{this.props.login}</div>
        );
    }
});

var renderMyPageLastLogin = function (createdAt, ip) {
    return React.renderToString(React.createElement(MyPageLastLogin, {createdAt: createdAt, ip: ip}));
};

var renderMyPageLogin = function (value) {
    return React.renderToString(React.createElement(MyPageLogin, {login: value}));
};
