package com.impl.catapp

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON
import static javax.servlet.http.HttpServletResponse.*

class CatController {

    static final int SC_UNPROCESSABLE_ENTITY = 422

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() { }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 5, 100)
        response.setIntHeader('X-Pagination-Total', Cat.count())
     //   println Cat.list(params) as JSON
        render Cat.list(params) as JSON
    }

    def save() {
        Cat cat = new Cat()
        cat.name = request.JSON.name
        cat.breed = request.JSON.breed
        cat.dateOfArrival = Date.parse("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", request.JSON.dateOfArrival.date)
        cat.coat = request?.JSON?.coat?.name
        Map responseJsonSave = [:]
        if (cat.save(flush: true)) {
            response.status = SC_CREATED
            responseJsonSave.id = cat.id
            responseJsonSave.message = message(code: 'default.created.message', args: [message(code: 'cat.label', default: 'Cat'), cat.id])
        } else {
            response.status = SC_UNPROCESSABLE_ENTITY
            responseJsonSave.errors = cat.errors.fieldErrors.collectEntries {
                [(it.field): message(error: it)]
            }
        }
        render responseJsonSave as JSON
    }

    def get() {
        Cat cat = Cat.get(params.id)
        if (cat) {
            render cat as JSON
        } else {
            notFound params.id
        }
    }

    def update() {
        Cat cat = Cat.get(params.id)
        if (!cat) {
            notFound params.id
            return
        }

        Map responseJson = [:]

        if (request.JSON.version != null) {
            if (cat.version > request.JSON.version) {
                response.status = SC_CONFLICT
                responseJson.message = message(code: 'default.optimistic.locking.failure',
                        args: [message(code: 'cat.label', default: 'Cat')],
                        default: 'Another user has updated this Cat while you were editing')
                cache false
                render responseJson as JSON
                return
            }
        }
        cat.name = request.JSON.name
        cat.breed = request.JSON.breed
        cat.coat = request.JSON?.coat.type
        try{
            cat.dateOfArrival = Date.parse("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", request.JSON.dateOfArrival.date)
        }
        catch(Exception e){
            cat.dateOfArrival = Date.parse("yyyy-MM-dd'T'hh:mm:ss'Z'", request.JSON.dateOfArrival.date)
        }

        if (cat.save(flush: true)) {
            response.status = SC_OK
            responseJson.id = cat.id
            responseJson.message = message(code: 'default.updated.message', args: [message(code: 'cat.label', default: 'Cat'), cat.id])
        } else {
            response.status = SC_UNPROCESSABLE_ENTITY
            responseJson.errors = cat.errors.fieldErrors.collectEntries {
                [(it.field): message(error: it)]
            }
        }

        render responseJson as JSON
    }

    def delete() {
        Cat cat = Cat.get(params.id)
        if (!cat) {
            notFound params.id
            return
        }

        Map responseJson = [:]
        try {
            cat.delete(flush: true)
            responseJson.message = message(code: 'default.deleted.message', args: [message(code: 'cat.label', default: 'Cat'), params.id])
        } catch (DataIntegrityViolationException e) {
            response.status = SC_CONFLICT
            responseJson.message = message(code: 'default.not.deleted.message', args: [message(code: 'cat.label', default: 'Cat'), params.id])
        }
        render responseJson as JSON
    }

    private void notFound(id) {
        response.status = SC_NOT_FOUND
        Map responseJson = [message: message(code: 'default.not.found.message', args: [message(code: 'cat.label', default: 'Cat'), params.id])]
        render responseJson as JSON
    }
}
