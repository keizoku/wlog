jQuery ($) ->

   # DOM element and DOM meta-data.
   $table = $('.container table')
   productListUrl = $table.data('list')

   # Load one column of id
   loadProductTable = ->
      $.get productListUrl, (records) ->
         $.each records, (index, id) ->
            row  = $('<tr/>').append $('<td/>').text(id)
            row.attr 'contenteditable', true
            $table.append row
            loadProductDetails row

   # Construct a URL by replacing the id parameter
   productDetailsUrl = (id) ->
      $table.data('details').replace 'abc', id

   # Load and append product details to a table row.
   loadProductDetails = (tableRow) ->
      id = tableRow.text()
      $.get productDetailsUrl(id), (record) ->
         tableRow.append $('<td/>').text(record.name)
         tableRow.append $('<td/>').text(record.address)
         tableRow.append $('<td/>')

   loadProductTable()


   # Save an edited table row to the server
   saveRow = ($row) ->
      [id, value, address2] = $row.children().map -> $(this).text()
      record =
         userid: id
         name: value
         address: address2
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
