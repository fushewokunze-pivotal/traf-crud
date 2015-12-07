package com.example

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FeedMessageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond FeedMessage.list(params), model:[feedMessageCount: FeedMessage.count()]
    }

    def show(FeedMessage feedMessage) {
        respond feedMessage
    }

    def create() {
        respond new FeedMessage(params)
    }

    @Transactional
    def save(FeedMessage feedMessage) {
        if (feedMessage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feedMessage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feedMessage.errors, view:'create'
            return
        }

        feedMessage.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'feedMessage.label', default: 'FeedMessage'), feedMessage.id])
                redirect feedMessage
            }
            '*' { respond feedMessage, [status: CREATED] }
        }
    }

    def edit(FeedMessage feedMessage) {
        respond feedMessage
    }

    @Transactional
    def update(FeedMessage feedMessage) {
        if (feedMessage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feedMessage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feedMessage.errors, view:'edit'
            return
        }

        feedMessage.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'feedMessage.label', default: 'FeedMessage'), feedMessage.id])
                redirect feedMessage
            }
            '*'{ respond feedMessage, [status: OK] }
        }
    }

    @Transactional
    def delete(FeedMessage feedMessage) {

        if (feedMessage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        feedMessage.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'feedMessage.label', default: 'FeedMessage'), feedMessage.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'feedMessage.label', default: 'FeedMessage'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
