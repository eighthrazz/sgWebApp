var background = background || (function(){
    var _args = {}; // private

    return {
        init : function(Args) {
            _args = Args;
        },
        set : function() {
            var url = '../img/bg/' + _args[0];
//            $('.navBackground')
//            .css('background', 'url(' + url + ') no-repeat center')
//            .css('background-size', 'cover')
//            .css('-webkit-background-size', 'cover')
//            .css('-moz-background-size', 'cover')
//            .css('-o-background-size', 'cover');
            $('.background')
            .css('background', 'url(' + url + ') no-repeat center')
            .css('background-size', 'cover')
            .css('-webkit-background-size', 'cover')
            .css('-moz-background-size', 'cover')
            .css('-o-background-size', 'cover');
        }
    };
}());

function ready() {
    console.log("ready");
    
    //
    $(".homeCalendar").load("calendar .calendar");
    
    //
    $(window).scroll(function() {
        // larger header
        if( $(document).scrollTop() <= 10 && 
            $('.sg-lg-header').css('display') == 'none'  ) 
        {
            $('.sg-lg-header').css('display', 'inline');
            $('.sg-sm-header').css('display', 'none');
            $('.navbar-header').css('text-align', 'center')
            $('.navbar-header').css('width' , '100%');
            $('.navbar-header').css('margin-top' , '0px');
        }
        
        // small header
        else if( $(document).scrollTop() > 10 && 
            $('.sg-sm-header').css('display') == 'none'  ) 
        {
            $('.sg-lg-header').css('display', 'none');
            $('.sg-sm-header').css('display', 'inline');
            $('.navbar-header').css('text-align', 'left');
            $('.navbar-header').css('width' , '50%');
            $('.navbar-header').css('margin-top' , '8px');
        }
    });
    
    $('#scroller').jscroll({
        loadingHtml: '<img src="../img/loading.gif" alt="Loading" />',
        debug: false,
        nextSelector: 'a.jscroll-next:last'
    });    
}
