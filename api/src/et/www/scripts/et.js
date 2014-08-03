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
    $scope.brandsByProduct = [];
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
              "categoryid": $scope.SelectedCategory.Id,
              "subcategoryid": $scope.SelectedSubCategory.Id,
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

    function searchByProduct(type, name, callback) {
        return $http.get ("/" + type + "_by_product?pid=" + $scope.SelectedProduct.Id).then(function (response)
        {
          callback(response.data);
          return response.data;
        });
    }


    function searchCategories(subcatid, callback) {
        return $http.get ("/cats_by_subcat?subcatid=" + subcatid).then(function (response)
        {
          callback(response.data);
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

    $scope.reloadCategories = function() {
       function selectFirstCat(data) { $scope.SelectedCategory = data[0]; }
       $scope.categoriesBySubCategory = searchCategories($scope.SelectedSubCategory.Id, selectFirstCat);
    };

    $scope.recalculate = function() {
       if ($scope.SelectedProduct == undefined)
          return;

       function selectFirstBrand(data) { $scope.SelectedBrand = data[0]; }
       $scope.brandsByProduct = searchByProduct("brands", "", selectFirstBrand);

       function selectFirstSubCat(data) { $scope.SelectedSubCategory = data[0]; $scope.reloadCategories(); }
       $scope.subcategoriesByProduct = searchByProduct("subcats", "", selectFirstSubCat);
    };

    $scope.clear = function() {
        $scope.recent_data = [];
    }
}
