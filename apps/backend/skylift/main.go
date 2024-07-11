package main

import (
	"context"
	"errors"
	"log"
	"net/http"
	"os"
	"os/signal"
	"shopez/skylift/config"
	"syscall"
	"time"
)

func main() {

	cfg, err := config.LoadConfig()
	if err != nil {
		log.Fatalf("failed to load config: %v", err)
		return
	}

	// inject dependencies
	router, err := inject(cfg)
	if err != nil {
		log.Fatalf("failed to inject dependencies: %v", err)
		return
	}

	// create a new HTTP server
	srv := &http.Server{
		Addr:    ":8000",
		Handler: router,
	}

	// graceful shutdown
	go func() {
		if err := srv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			log.Fatalf("failed to initialize server: %v", err)
		}
	}()

	log.Printf("INFO: server is running on %s\n", srv.Addr)

	// wait for interrupt signal to gracefully shut down the server
	quit := make(chan os.Signal)

	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	// the context is used to inform the server it has 5 seconds to finish
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	log.Printf("INFO: shutting down server...\n")
	if err := srv.Shutdown(ctx); err != nil {
		log.Printf("INFO: server forced to shutdown: %v", err)
		return
	}
}
