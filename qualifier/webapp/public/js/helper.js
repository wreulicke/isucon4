Handlebars.registerHelper('dateFormat', function (context, block) {
    if (window.moment) {
        var d = String(context).substring(0, 19);
        var f = block.hash.format || 'YYYY-MM-DD HH:mm:ss';
        return moment(d).format(f);
    } else {
        return context;
    }
});
