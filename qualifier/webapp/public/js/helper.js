var dateFormat = function (context, format) {
    if (window.moment) {
        var d = String(context).substring(0, 19);
        var f = format || 'YYYY-MM-DD HH:mm:ss';
        return moment(d).format(f);
    } else {
        return context;
    }
};
