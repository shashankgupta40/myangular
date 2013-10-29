package com.impl.catapp

import grails.test.mixin.*

@TestFor(CatController)
@Mock(Cat)
class CatControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/cat/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.catInstanceList.size() == 0
        assert model.catInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.catInstance != null
    }

    void testSave() {
        controller.save()

        assert model.catInstance != null
        assert view == '/cat/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/cat/show/1'
        assert controller.flash.message != null
        assert Cat.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/cat/list'

        populateValidParams(params)
        def cat = new Cat(params)

        assert cat.save() != null

        params.id = cat.id

        def model = controller.show()

        assert model.catInstance == cat
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/cat/list'

        populateValidParams(params)
        def cat = new Cat(params)

        assert cat.save() != null

        params.id = cat.id

        def model = controller.edit()

        assert model.catInstance == cat
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/cat/list'

        response.reset()

        populateValidParams(params)
        def cat = new Cat(params)

        assert cat.save() != null

        // test invalid parameters in update
        params.id = cat.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/cat/edit"
        assert model.catInstance != null

        cat.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/cat/show/$cat.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        cat.clearErrors()

        populateValidParams(params)
        params.id = cat.id
        params.version = -1
        controller.update()

        assert view == "/cat/edit"
        assert model.catInstance != null
        assert model.catInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/cat/list'

        response.reset()

        populateValidParams(params)
        def cat = new Cat(params)

        assert cat.save() != null
        assert Cat.count() == 1

        params.id = cat.id

        controller.delete()

        assert Cat.count() == 0
        assert Cat.get(cat.id) == null
        assert response.redirectedUrl == '/cat/list'
    }
}
