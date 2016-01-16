var IndexComponent = React.createClass({displayName: "IndexComponent",
    getInitialState: function () {
        return {
            login: this.props.login,
            password: this.props.password
        };
    },
    render: function () {
        return (
            React.createElement("div", {className: "container"}, 
                React.createElement("h1", {id: "topbar"}, 
                    React.createElement("a", {href: "/"}, React.createElement("img", {src: "/images/isucon-bank.png", alt: "いすこん銀行 オンラインバンキングサービス"}))
                ), 

                React.createElement("div", {id: "be-careful-phising", className: "panel panel-danger"}, 
                    React.createElement("div", {className: "panel-heading"}, 
                        React.createElement("span", {className: "hikaru-mozi"}, "偽画面にご注意ください！")
                    ), 
                    React.createElement("div", {className: "panel-body"}, 
                        React.createElement("p", null, "偽のログイン画面を表示しお客様の情報を盗み取ろうとする犯罪が多発しています。"), 

                        React.createElement("p", null, "ログイン直後にダウンロード中や、見知らぬウィンドウが開いた場合、", React.createElement("br", null), "すでにウィルスに感染している場合がございます。即座に取引を中止してください。"), 

                        React.createElement("p", null, "また、残高照会のみなど、必要のない場面で乱数表の入力を求められても、", React.createElement("br", null), "絶対に入力しないでください。")
                    )
                ), 

                React.createElement("div", {className: "page-header"}, 
                    React.createElement("h1", null, "ログイン")
                ), 

                (this.props.msg ? function () {
                    return React.createElement("div", {id: "notice-message", className: "alert alert-danger", role: "alert"}, this.props.msg);
                } : function () {
                    return null;
                }).call(this), 

                React.createElement("div", {className: "container"}, 
                    React.createElement("form", {className: "form-horizontal", role: "form", action: "/login", method: "POST"}, 
                        React.createElement("div", {className: "form-group"}, 
                            React.createElement("label", {for: "input-username", className: "col-sm-3 control-label"}, "お客様ご契約ID"), 

                            React.createElement("div", {className: "col-sm-9"}, 
                                React.createElement("input", {id: "input-username", type: "text", className: "form-control", placeholder: "半角英数字", 
                                       name: "login", 
                                       value: this.state.login, onChange: this.changeLogin})
                            )
                        ), 
                        React.createElement("div", {className: "form-group"}, 
                            React.createElement("label", {for: "input-password", className: "col-sm-3 control-label"}, "パスワード"), 

                            React.createElement("div", {className: "col-sm-9"}, 
                                React.createElement("input", {type: "password", className: "form-control", id: "input-password", name: "password", 
                                       placeholder: "半角英数字・記号（２文字以上）", value: this.state.password, 
                                       onChange: this.changePassword})
                            )
                        ), 
                        React.createElement("div", {className: "form-group"}, 
                            React.createElement("div", {className: "col-sm-offset-3 col-sm-9"}, 
                                React.createElement("button", {type: "submit", className: "btn btn-primary btn-lg btn-block"}, "ログイン")
                            )
                        )
                    )
                )
            )
        );
    },
    changeLogin: function (e) {
        this.setState({login: e.target.value});
    },
    changePassword: function (e) {
        this.setState({password: e.target.value});
    }
});
