package main

import (
	"context"
	"errors"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"shopez/darkshield/config"
	"syscall"
	"time"
)

func main() {
	cfg, err := config.LoadConfig()
	if err != nil {
		log.Fatalf("failed to load config: %v", err)
	}

	fmt.Printf("cfg: %+v\n", cfg)

	router, err := inject(cfg)
	if err != nil {
		log.Fatalf("failed to inject dependencies: %v", err)
	}

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

	log.Printf("server is running on %s\n", srv.Addr)

	// wait for interrupt signal to gracefully shut down the server with a timeout of 5 seconds
	quit := make(chan os.Signal)

	// kill (no param) default send syscall.SIGTERM
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	// the context is used to inform the server it has 5 seconds to finish
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	// shutdown the server
	log.Printf("shutting down server...\n")
	if err := srv.Shutdown(ctx); err != nil {
		log.Fatalf("server forced to shutdown: %v", err)
	}
}
