<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${haus.name}</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Sitz</h3>
                    <hr />
                    <p>${haus.burgName} (${haus.ortName} <a href="/location/${haus.ortId}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a>)</p>
                </div>
                <div class="col-lg-12">
                    <h3>Personen</h3>
                    <hr />
                    <p>
                    <#list haus.members as member>
                        ${member.name} <a href="/person/${member.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Besitz</h3>
                    <hr />
                    <p>
                    <#list haus.properties as property>
                    ${property.name} <a href="/location/${property.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <hr />
                <#assign ratingData=haus.ratingData>
                <#include "rating.ftl" parse=true>
                </div>
            </div>
        </div>
    </div>
</section>