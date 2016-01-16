var MyPageComponent = React.createClass({displayName: "MyPageComponent",
    render: function () {
        return (
            React.createElement("div", {className: "container"}, 
                React.createElement("h1", {id: "topbar"}, 
                    React.createElement("a", {href: "/"}, React.createElement("img", {src: "/images/isucon-bank.png", alt: "いすこん銀行 オンラインバンキングサービス"}))
                ), 

                React.createElement("div", {className: "alert alert-success", role: "alert"}, 
                    "ログインに成功しました。", React.createElement("br", null), 
                    "未読のお知らせが０件、残っています。"
                ), 

                React.createElement("dl", {className: "dl-horizontal"}, 
                    React.createElement("dt", null, "前回ログイン"), 
                    React.createElement("dd", {id: "last-logined-at"}, dateFormat(this.props.createdAt)), 
                    React.createElement("dt", null, "最終ログインIPアドレス"), 
                    React.createElement("dd", {id: "last-logined-ip"}, this.props.ip)
                ), 

                React.createElement("div", {className: "panel panel-default"}, 
                    React.createElement("div", {className: "panel-heading"}, this.props.login), 
                    React.createElement("div", {className: "panel-body"}, 
                        React.createElement("div", {className: "row"}, 
                            React.createElement("div", {className: "col-sm-4"}, 
                                "普通預金", React.createElement("br", null), 
                                React.createElement("small", null, "東京支店 1111111111"), 
                                React.createElement("br", null)
                            ), 
                            React.createElement("div", {className: "col-sm-4"}, 
                                React.createElement("p", {id: "zandaka", className: "text-right"}, 
                                    "―――円"
                                )
                            ), 

                            React.createElement("div", {className: "col-sm-4"}, 
                                React.createElement("p", null, 
                                    React.createElement("a", {className: "btn btn-success btn-block"}, "入出金明細を表示"), 
                                    React.createElement("a", {className: "btn btn-default btn-block"}, "振込・振替はこちらから")
                                )
                            ), 

                            React.createElement("div", {className: "col-sm-12"}, 
                                React.createElement("a", {className: "btn btn-link btn-block"}, "定期預金・住宅ローンのお申込みはこちら")
                            )
                        )
                    )
                )
            )
        );
    }
});
