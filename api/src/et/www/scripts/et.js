var app = angular.module('track', ['ui.bootstrap']);
app.directive('ngBlur', function() {
  return function( scope, elem, attrs ) {
    elem.bind('blur', function() {
      scope.$apply(attrs.ngBlur);
    });
  };
});
function EtCtrl ($http, $scope) {
    $scope.navType = "tabs";
    $scope.alerts = [];
    $scope.selectedProduct = undefined;
    $scope.selectedBrand = undefined;
    $scope.selectedShop = undefined;
    
    $scope.save_thing_to_buy = function() {
        $http.post ("/add",
                { "item" : $scope.item_to_buy }).success (function (response) {
                  $scope.items_to_buy = response;
                  $scope.item_to_buy = '';
                });
    };

    $scope.save = function() {
        $http.post ("/save", 
            { "orderdate" : $scope.dt,
              "productid" : $scope.SelectedProduct.Id,
              "brandid"   : $scope.SelectedBrand.Id,
              "shopid"    : $scope.SelectedShop.Id,
              "quantity"  : $scope.quantity,
              "comments"  : $scope.comments,
              "price" : $scope.price,
              }).success (function (data) {
                $scope.alerts = [];
                $scope.alerts.push ( {msg: data.Msg } );
        });
    };
    
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
        $scope.item = $scope.quantity = $scope.price = $scope.vendor = '';
    };

    $scope.today = function() { $scope.dt = new Date(); }
    $scope.today();
    $scope.initDate = new Date();

    $scope.clear = function() {  $scope.dt = null; }

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };
    
    function search(type, name) {
        return $http.get ("/" + type + "?name=" + name).then(function (response)
        {
          return response.data;
        });
    }

    $scope.getItemsToBuy = function() {
        $http.get ("/get_items_to_buy").then(function (response)
            {
                $scope.items_to_buy = response.data;
            });
    }

    $scope.getMonthlySpendings = function() {
        $http.get ("/get_monthly_spendings").then(function (response)
            {
                $scope.monthTotals = response.data;
            });
    }
    
    $scope.removeFromItemsToBuy = function(index) {
        $http.post ("/remove_item_to_buy", { 
            "item" : $scope.items_to_buy[index] }).success (function (response) 
                { $scope.items_to_buy = response; });
    };
    
    $scope.getProducts = function(productName) {
        return search ("products", productName);
    };
    
    $scope.getBrands = function(brandName) {
        return search ("brands", brandName);
    };


    $scope.getShops = function(shopName) {
        return search ("shops", shopName);
    };

    $scope.getRecentPurchasesOfItem = function() {
        $http.post ("/recent", 
            { "item" : $scope.item }).success (function (data) {
              $scope.recent_data = data;
            });
    };

    $scope.recalculate = function() {
       for (i = 0; i< $scope.recent_data.length; ++i)
       {
            $scope.recent_data[i].ComparitivePrice = $scope.quantity * $scope.recent_data[i].NormalizedPrice;
       }
    }

    $scope.clear = function() {
        $scope.recent_data = [];
    }
    
    $scope.getItemsToBuy();
    $scope.getMonthlySpendings();
}
