var app = angular.module('et', ['ui.bootstrap']);
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
    $scope.SelectedProduct = undefined;
    $scope.SelectedBrand = undefined;
    $scope.SelectedShop = undefined;
    $scope.SelectedCateogry = undefined;
    $scope.saveButtonText = "Save";
    $scope.subcategoriesByProduct = [ { "Id" : -1, "Value" : "None" } ];
    $scope.SelectedSubCategory = $scope.subcategoriesByProduct[0];
    
    $scope.save = function() {
        $scope.saveButtonText = "Saving";
        $http.post ("/save", 
            { "orderdate" : $scope.dt,
              "productid" : $scope.SelectedProduct.Id,
              "brandid"   : $scope.SelectedBrand ? $scope.SelectedBrand.Id : -1,
              "categoryid": $scope.SelectedCategory.Id,
              "subcategoryid": $scope.SelectedSubCategory.Id,
              "shopid"    : $scope.SelectedShop ? $scope.SelectedShop.Id : -1,
              "quantity"  : $scope.Quantity,
              "comments"  : $scope.Comments,
              "price" : $scope.Price,
              }).success (function (data) {
                $scope.alerts = [];
                $scope.alerts.push ( {msg: data.Msg } );
        }).then (function ()
        {
	    $scope.saveButtonText = "Save";
        });
        alert ($http.pendingRequests.length);
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


    function query (q) {
        return $http.get (q).then(function (response)
        {
          return response.data;
        });
    }

    function search(type, name) { return query ("/" + type + "?name=" + name) }

    function searchByProduct(product, type) {
        return query ("/" + type + "_by_product?pid=" + product.Id)
    }

    function searchCategories(subcatid) {
        return query ("/cats_by_subcat?subcatid=" + subcatid)
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
        searchCategories($scope.SelectedSubCategory.Id).then (function (data)
        {
            $scope.categoriesBySubCategory = data;
	    $scope.SelectedCategory = $scope.categoriesBySubCategory[0];
	});
    };

    $scope.recalculate = function(item, model, label) {
       if (model == undefined)
          return;

       searchByProduct(model, "brands").then (function (data)
       {
	   $scope.brandsByProduct = data;
           $scope.SelectedBrand = $scope.brandsByProduct[0];
       });

       searchByProduct(model, "subcats").then (function (data)
       {
           $scope.subcategoriesByProduct = data;
           $scope.SelectedSubCategory = $scope.subcategoriesByProduct[0];
           $scope.reloadCategories();
       });
    };

    $scope.clear = function() {
        $scope.recent_data = [];
    }
}
