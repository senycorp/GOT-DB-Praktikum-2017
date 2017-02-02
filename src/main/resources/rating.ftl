<div class="text-center">
    <h3>Ratings</h3>
    <hr class="star-primary">
    <h5>Total Rating: ${ratingData.totalRating}</h5>
</div>
<div class="col-lg-6 col-lg-offset-3">
    <hr />
    <form class="form-horizontal" type="GET">
        <div class="form-group">
            <label for="inputEmail3" class="col-sm-4 control-label">Your Rating</label>
            <div class="col-sm-8">
                <select id="rating" class="form-control">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <textarea class="form-control" id="ratingText"></textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <button type="button" onClick="saveRating(${ratingData.id}, $('#rating').val(), $('#ratingText').val()) " class="btn btn-primary btn-block"><i class="fa fa-save"></i> Save</button>
            </div>
        </div>
    </form>
    <hr />
    <#list ratingData.data as rating>
        <div class="alert alert-info">
            <span class="label label-info">Author: ${rating.name}</span> <span class="label label-warning pull-right">Rating: ${rating.rating}</span>
            <hr />
            ${rating.text}
        </div>
    </#list>
</div>