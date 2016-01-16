var IndexComponent = React.createClass({
    getInitialState: function () {
        return {
            login: this.props.login,
            password: this.props.password
        };
    },
    render: function () {
        return (
            <div className="container">
                <h1 id="topbar">
                    <a href="/"><img src="/images/isucon-bank.png" alt="いすこん銀行 オンラインバンキングサービス"/></a>
                </h1>

                <div id="be-careful-phising" className="panel panel-danger">
                    <div className="panel-heading">
                        <span className="hikaru-mozi">偽画面にご注意ください！</span>
                    </div>
                    <div className="panel-body">
                        <p>偽のログイン画面を表示しお客様の情報を盗み取ろうとする犯罪が多発しています。</p>

                        <p>ログイン直後にダウンロード中や、見知らぬウィンドウが開いた場合、<br/>すでにウィルスに感染している場合がございます。即座に取引を中止してください。</p>

                        <p>また、残高照会のみなど、必要のない場面で乱数表の入力を求められても、<br/>絶対に入力しないでください。</p>
                    </div>
                </div>

                <div className="page-header">
                    <h1>ログイン</h1>
                </div>

                {(this.props.msg ? function () {
                    return <div id="notice-message" className="alert alert-danger" role="alert">{this.props.msg}</div>;
                } : function () {
                    return null;
                }).call(this)}

                <div className="container">
                    <form className="form-horizontal" role="form" action="/login" method="POST">
                        <div className="form-group">
                            <label for="input-username" className="col-sm-3 control-label">お客様ご契約ID</label>

                            <div className="col-sm-9">
                                <input id="input-username" type="text" className="form-control" placeholder="半角英数字"
                                       name="login"
                                       value={this.state.login} onChange={this.changeLogin}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label for="input-password" className="col-sm-3 control-label">パスワード</label>

                            <div className="col-sm-9">
                                <input type="password" className="form-control" id="input-password" name="password"
                                       placeholder="半角英数字・記号（２文字以上）" value={this.state.password}
                                       onChange={this.changePassword}/>
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="col-sm-offset-3 col-sm-9">
                                <button type="submit" className="btn btn-primary btn-lg btn-block">ログイン</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        );
    },
    changeLogin: function (e) {
        this.setState({login: e.target.value});
    },
    changePassword: function (e) {
        this.setState({password: e.target.value});
    }
});

var renderIndexComponent = function (msg, login, password) {
    return React.renderToString(
        React.createElement(
            IndexComponent,
            {
                msg: msg,
                login: login,
                password: password
            }
        ));
};
