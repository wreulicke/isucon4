var MyPageComponent = React.createClass({
    render: function () {
        return (
            <div className="container">
                <h1 id="topbar">
                    <a href="/"><img src="/images/isucon-bank.png" alt="いすこん銀行 オンラインバンキングサービス"/></a>
                </h1>

                <div className="alert alert-success" role="alert">
                    ログインに成功しました。<br/>
                    未読のお知らせが０件、残っています。
                </div>

                <dl className="dl-horizontal">
                    <dt>前回ログイン</dt>
                    <dd id="last-logined-at">{dateFormat(this.props.createdAt)}</dd>
                    <dt>最終ログインIPアドレス</dt>
                    <dd id="last-logined-ip">{this.props.ip}</dd>
                </dl>

                <div className="panel panel-default">
                    <div className="panel-heading">{this.props.login}</div>
                    <div className="panel-body">
                        <div className="row">
                            <div className="col-sm-4">
                                普通預金<br/>
                                <small>東京支店 1111111111</small>
                                <br/>
                            </div>
                            <div className="col-sm-4">
                                <p id="zandaka" className="text-right">
                                    ―――円
                                </p>
                            </div>

                            <div className="col-sm-4">
                                <p>
                                    <a className="btn btn-success btn-block">入出金明細を表示</a>
                                    <a className="btn btn-default btn-block">振込・振替はこちらから</a>
                                </p>
                            </div>

                            <div className="col-sm-12">
                                <a className="btn btn-link btn-block">定期預金・住宅ローンのお申込みはこちら</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

var renderMyPageComponent = function (createdAt, ip, login) {
    return React.renderToString(
        React.createElement(
            MyPageComponent,
            {
                createdAt: createdAt,
                ip: ip,
                login: login
            }
        ));
};
