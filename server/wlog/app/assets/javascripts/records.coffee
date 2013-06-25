jQuery ($) ->

   # DOM element and DOM meta-data.
   $table = $('.container table')
   productListUrl = $table.data('list')

   # Load one column of EAN codes
   loadProductTable = ->
      $.get productListUrl, (records) ->
         $.each records, (index, id) ->
            row  = $('<tr/>').append $('<td/>').text(id)
            row.attr 'contenteditable', true
            $table.append row
            loadProductDetails row

   # Construct a URL by replacing the EAN code parameter
   productDetailsUrl = (id) ->
      $table.data('details').replace '0', id

   # Load and append product details to a table row.
   loadProductDetails = (tableRow) ->
      id = tableRow.text()
      $.get productDetailsUrl(id), (record) ->
         tableRow.append $('<td/>').text(record.value)

   loadProductTable()


   # Save an edited table row to the server
   saveRow = ($row) ->
      [id, value] = $row.children().map -> $(this).text()
      record =
         id: parseInt(id)
         value: value
      jqxhr = $.ajax
         type: "PUT"
         url: productDetailsUrl(id)
         contentType: "application/json"
         data: JSON.stringify record
      jqxhr.done (response) ->
         $label = $('<span/>').addClass('label label-success')
         $row.children().last().append $label.text(response)
         $label.delay(3000).fadeOut()
      jqxhr.fail (data) ->
         $label = $('<span/>').addClass('label label-important')
         message = data.responseText || data.statusText
         $row.children().last().append $label.text(message)

   # Attach edit handling to editable elements.
   $('[contenteditable]').live 'blur', ->
         saveRow $(this)
