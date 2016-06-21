var background = background || (function(){
    var _args = {}; // private

    return {
        init : function(Args) {
            _args = Args;
        },
        set : function() {
            var url = '../img/bg/' + _args[0];
            $('.background')
            .css('background', 'url(' + url + ') no-repeat center fixed')
            .css('background-size', 'cover')
            .css('-webkit-background-size', 'cover')
            .css('-moz-background-size', 'cover')
            .css('-o-background-size', 'cover');
        }
    };
}());

function docFill() {
    if ($(document).height() <= $(window).height()) {
        $(".docFill").css(
                "height",
                $(window).height() - $("body").height() - $(".header").height()
                        + $(".docFill").height() );
    } else {
        $(".docFill").css("height", "auto");
    }
};

function ready() {
    //
    $(".homeCalendar").load("calendar .calendar");
    
    //
    $(".homeNews").load("news .news");
    
    $(document).ready(docFill);
    $(window).resize(docFill);
}

